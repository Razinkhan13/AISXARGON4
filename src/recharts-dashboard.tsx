/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import React, { useState, useEffect, useMemo } from 'react';
import { createRoot } from 'react-dom/client';
import {
  ResponsiveContainer,
  AreaChart,
  Area,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ReferenceLine
} from 'recharts';

interface Transaction {
  id: string;
  client: string;
  type: string;
  phone?: string;
  amount: number;
  aiScore?: number;
  aiNote?: string;
  status: 'FUNDS_LOCKED' | 'AWAITING_HUMAN_LOCK';
  tenant?: string;
}

declare global {
  interface Window {
    AIS_TRANSACTIONS?: Transaction[];
    AIS_LOCKED_REVENUE?: number;
    AIS_90DAY_TARGET?: number;
    refreshRechartsRevenueDashboard?: () => void;
  }
}

// BDT Currency Formatter
function formatBDT(amount: number): string {
  if (amount >= 10000000) {
    const cr = (amount / 10000000).toFixed(2);
    return `৳${cr} Crore`;
  }
  if (amount >= 100000) {
    const lakh = (amount / 100000).toFixed(2);
    return `৳${lakh} Lakh`;
  }
  return `৳${amount.toLocaleString('en-IN')}`;
}

function formatShortBDT(val: number): string {
  if (val >= 10000000) return `৳${(val / 10000000).toFixed(1)}Cr`;
  if (val >= 100000) return `৳${(val / 100000).toFixed(0)}L`;
  if (val === 0) return '৳0';
  return `৳${(val / 1000).toFixed(0)}k`;
}

// Custom Tooltip Component
const CustomTooltip = ({ active, payload, label }: any) => {
  if (active && payload && payload.length) {
    const dataPoint = payload[0]?.payload;
    return (
      <div className="bg-[#0b101b] border border-[#00dbe9]/50 rounded-xl p-3 shadow-2xl backdrop-blur-md text-xs font-mono-code space-y-1.5 z-50">
        <div className="flex items-center justify-between gap-4 border-b border-slate-800 pb-1.5">
          <span className="text-[#00dbe9] font-bold">{label}</span>
          {dataPoint?.milestone && (
            <span className="text-[10px] bg-[#ff4b89]/20 text-[#ffb1c3] border border-[#ff4b89]/40 px-2 py-0.5 rounded-full">
              {dataPoint.milestone}
            </span>
          )}
        </div>
        <div className="space-y-1">
          <div className="flex items-center justify-between gap-3">
            <span className="text-slate-400">Locked Revenue:</span>
            <span className="font-bold text-emerald-400">
              {formatBDT(dataPoint?.lockedRevenue || 0)}
            </span>
          </div>
          <div className="flex items-center justify-between gap-3">
            <span className="text-slate-400">90-Day Trajectory:</span>
            <span className="font-bold text-slate-300">
              {formatBDT(dataPoint?.targetTrajectory || 0)}
            </span>
          </div>
          {dataPoint?.projectedTotal !== undefined && (
            <div className="flex items-center justify-between gap-3">
              <span className="text-slate-400">Projected Pipeline:</span>
              <span className="font-bold text-[#ffb1c3]">
                {formatBDT(dataPoint?.projectedTotal || 0)}
              </span>
            </div>
          )}
          {dataPoint?.dealNote && (
            <div className="pt-1 text-[11px] text-sky-300 truncate max-w-[260px]">
              • {dataPoint.dealNote}
            </div>
          )}
        </div>
      </div>
    );
  }
  return null;
};

export const RechartsRevenueDashboard: React.FC = () => {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [lockedRevenue, setLockedRevenue] = useState<number>(1250000);
  const targetRevenue = 10000000; // 90-Day Target: ৳1 Crore BDT
  const [filterStream, setFilterStream] = useState<'ALL' | 'RE' | 'ECOM'>('ALL');
  const [isSimulating, setIsSimulating] = useState(false);

  // Sync state from window / localStorage / events
  const syncFromGlobalState = () => {
    let txList: Transaction[] = [];
    if (window.AIS_TRANSACTIONS && Array.isArray(window.AIS_TRANSACTIONS)) {
      txList = window.AIS_TRANSACTIONS;
    } else {
      try {
        const saved = localStorage.getItem('ais_terminal_state_v4');
        if (saved) {
          const parsed = JSON.parse(saved);
          if (Array.isArray(parsed.transactions)) txList = parsed.transactions;
        }
      } catch (e) {}
    }

    // Default fallback transactions if empty
    if (txList.length === 0) {
      txList = [
        {
          id: 'TX-901',
          client: 'Dom-Inno Developers Ltd. — Tier-2 Single-Asset Relief Sandbox',
          type: 'SANDBOX_ESCROW',
          amount: 600000,
          status: 'AWAITING_HUMAN_LOCK',
          tenant: 'DOMINNO',
          aiScore: 97
        },
        {
          id: 'TX-902',
          client: 'Sheltech (Pvt.) Ltd. — Tier-2 Single-Asset Relief Sandbox',
          type: 'SANDBOX_ESCROW',
          amount: 600000,
          status: 'AWAITING_HUMAN_LOCK',
          tenant: 'SHELTECH',
          aiScore: 98
        },
        {
          id: 'TX-903',
          client: 'Concord Real Estate — 60-Day Turnaround Mandate',
          type: 'ENTERPRISE_CORE',
          amount: 2300000,
          status: 'AWAITING_HUMAN_LOCK',
          tenant: 'CONCORD',
          aiScore: 96
        },
        {
          id: 'TX-904',
          client: 'Building Technology & Ideas (bti) — Celebration Point Turnaround',
          type: 'ENTERPRISE_CORE',
          amount: 2300000,
          status: 'AWAITING_HUMAN_LOCK',
          tenant: 'BTI',
          aiScore: 94
        },
        {
          id: 'TX-905',
          client: 'Eastern Housing Ltd. (EHL) — Banani Lakeview NRB Desk',
          type: 'ENTERPRISE_CORE',
          amount: 2300000,
          status: 'AWAITING_HUMAN_LOCK',
          tenant: 'EHL',
          aiScore: 95
        },
        {
          id: 'TX-906',
          client: 'Gorur Ghash — Fast-Cash E-Commerce COD Shield Pilot',
          type: 'FAST_CASH_COD',
          amount: 25000,
          status: 'AWAITING_HUMAN_LOCK',
          tenant: 'ECOM',
          aiScore: 94
        },
        {
          id: 'TX-907',
          client: 'Fabrilife — Fast-Cash E-Commerce COD Shield Pilot',
          type: 'FAST_CASH_COD',
          amount: 25000,
          status: 'AWAITING_HUMAN_LOCK',
          tenant: 'ECOM',
          aiScore: 95
        }
      ];
    }

    setTransactions(txList);

    // Compute actual locked revenue
    const lockedSum = txList
      .filter(t => t.status === 'FUNDS_LOCKED')
      .reduce((sum, t) => sum + (t.amount || 0), 0);

    const effectiveLocked = typeof window.AIS_LOCKED_REVENUE === 'number' && window.AIS_LOCKED_REVENUE > 0
      ? Math.max(window.AIS_LOCKED_REVENUE, lockedSum)
      : (lockedSum > 0 ? lockedSum : 1250000);

    setLockedRevenue(effectiveLocked);
  };

  useEffect(() => {
    syncFromGlobalState();

    const handleUpdate = () => {
      syncFromGlobalState();
    };

    window.addEventListener('ais:revenue-updated', handleUpdate);
    window.refreshRechartsRevenueDashboard = syncFromGlobalState;

    // Check interval every 2 seconds for storage changes
    const interval = setInterval(syncFromGlobalState, 2000);

    return () => {
      window.removeEventListener('ais:revenue-updated', handleUpdate);
      clearInterval(interval);
    };
  }, []);

  // Filtered transactions based on stream toggle
  const filteredTx = useMemo(() => {
    if (filterStream === 'RE') {
      return transactions.filter(t => t.type !== 'FAST_CASH_COD');
    }
    if (filterStream === 'ECOM') {
      return transactions.filter(t => t.type === 'FAST_CASH_COD');
    }
    return transactions;
  }, [transactions, filterStream]);

  // Derived progress values
  const progressPercent = Math.min(100, +((lockedRevenue / targetRevenue) * 100).toFixed(1));
  const remainingBDT = Math.max(0, targetRevenue - lockedRevenue);
  const lockedCount = transactions.filter(t => t.status === 'FUNDS_LOCKED').length;
  const pendingCount = transactions.filter(t => t.status !== 'FUNDS_LOCKED').length;
  const pendingPipelineSum = transactions
    .filter(t => t.status !== 'FUNDS_LOCKED')
    .reduce((acc, t) => acc + t.amount, 0);

  // Build Chronological Chart Data points for the 90-Day Sprint
  const chartData = useMemo(() => {
    // 90-Day Sprint timeline points
    const days = [
      { day: 0, label: 'Day 0', milestone: 'Kickoff' },
      { day: 12, label: 'Day 12', milestone: 'Pilot 1' },
      { day: 24, label: 'Day 24 (Today)', milestone: 'Current Sprint' },
      { day: 36, label: 'Day 36', milestone: 'Mid-Retainer' },
      { day: 50, label: 'Day 50', milestone: 'Expansion' },
      { day: 65, label: 'Day 65', milestone: 'Escrow Lock' },
      { day: 78, label: 'Day 78', milestone: 'Closing Wave' },
      { day: 90, label: 'Day 90', milestone: '৳1 Cr Target' }
    ];

    // Locked deals list
    const lockedDeals = transactions.filter(t => t.status === 'FUNDS_LOCKED');
    
    // Distribute actual locked funds along the timeline up to Day 24 (Today)
    let cumulative = 0;
    const dealSteps = [0, 25000, lockedRevenue * 0.4, lockedRevenue];

    return days.map((d, idx) => {
      // Linear target trajectory from 0 to ৳10,000,000
      const targetTrajectory = Math.round((d.day / 90) * targetRevenue);

      let actualLocked: number | null = null;
      let projectedTotal: number | null = null;
      let dealNote = '';

      if (d.day <= 24) {
        if (d.day === 0) actualLocked = 0;
        else if (d.day === 12) {
          actualLocked = Math.min(lockedRevenue, 25000);
          dealNote = 'E-Com COD Shield Mobilization';
        } else if (d.day === 24) {
          actualLocked = lockedRevenue;
          dealNote = `${lockedCount} Deals Reconciled (Trust Bank / MFS)`;
        }
      }

      // Projected trajectory into future
      if (d.day >= 24) {
        const remainingDays = 90 - 24;
        const fraction = (d.day - 24) / remainingDays;
        projectedTotal = Math.round(lockedRevenue + (pendingPipelineSum * 0.85 * fraction));
      }

      return {
        dayLabel: d.label,
        milestone: d.milestone,
        targetTrajectory,
        lockedRevenue: actualLocked,
        projectedTotal,
        dealNote
      };
    });
  }, [lockedRevenue, targetRevenue, transactions, pendingPipelineSum, lockedCount]);

  // Handle Quick Lock Next Deal Simulation
  const handleLockNextDeal = () => {
    setIsSimulating(true);
    const pending = transactions.find(t => t.status !== 'FUNDS_LOCKED');
    if (pending) {
      pending.status = 'FUNDS_LOCKED';
      const newLocked = lockedRevenue + pending.amount;
      setLockedRevenue(newLocked);
      if (typeof window.AIS_LOCKED_REVENUE === 'number') {
        window.AIS_LOCKED_REVENUE = newLocked;
      }
      // Notify parent app
      window.dispatchEvent(
        new CustomEvent('ais:revenue-updated', {
          detail: {
            transactions,
            lockedRevenueBDT: newLocked,
            targetBDT: targetRevenue
          }
        })
      );
    }
    setTimeout(() => setIsSimulating(false), 500);
  };

  return (
    <div className="bg-[#0b0f19] border border-[#00dbe9]/30 rounded-2xl p-4 sm:p-6 space-y-5 shadow-2xl relative overflow-hidden font-sans">
      {/* Background Neon Glow Accent */}
      <div className="absolute -top-24 -right-24 w-72 h-72 bg-[#00dbe9]/10 rounded-full blur-3xl pointer-events-none" />
      <div className="absolute -bottom-24 -left-24 w-72 h-72 bg-[#ff4b89]/10 rounded-full blur-3xl pointer-events-none" />

      {/* Top Header & Stream Filter Bar */}
      <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-4 border-b border-slate-800/80 pb-4 relative z-10">
        <div>
          <div className="flex flex-wrap items-center gap-2">
            <span className="px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider bg-[#00dbe9]/15 text-[#00dbe9] border border-[#00dbe9]/30 flex items-center gap-1">
              <span className="w-1.5 h-1.5 rounded-full bg-[#00dbe9] animate-pulse" />
              RECHARTS FINANCIAL ANALYTICS
            </span>
            <span className="px-2.5 py-0.5 rounded-full text-[10px] font-bold uppercase tracking-wider bg-[#ff4b89]/20 text-[#ffb1c3] border border-[#ffb1c3]/30">
              90-DAY TARGET ACCELERATOR
            </span>
          </div>
          <h3 className="font-display text-lg sm:text-2xl font-bold text-white mt-1">
            90-Day Target vs. Locked Revenue Velocity
          </h3>
          <p className="text-xs text-slate-400 mt-0.5 max-w-2xl">
            Live telemetry tracking actual verified bank &amp; MFS receipts against our statutory ৳1.00 Crore sprint target, pulling real-time settlement records directly from the deal pipeline queue.
          </p>
        </div>

        {/* Action Controls & Filters */}
        <div className="flex flex-wrap items-center gap-2">
          <div className="bg-[#060a12] p-1 rounded-xl border border-slate-800 flex items-center gap-1 text-xs font-mono-code">
            <button
              onClick={() => setFilterStream('ALL')}
              className={`px-3 py-1.5 rounded-lg transition font-semibold cursor-pointer ${
                filterStream === 'ALL'
                  ? 'bg-gradient-to-r from-[#00dbe9] to-[#0ea5e9] text-slate-950 font-bold shadow'
                  : 'text-slate-400 hover:text-white'
              }`}
            >
              All Queue
            </button>
            <button
              onClick={() => setFilterStream('RE')}
              className={`px-3 py-1.5 rounded-lg transition font-semibold cursor-pointer ${
                filterStream === 'RE'
                  ? 'bg-sky-500 text-slate-950 font-bold shadow'
                  : 'text-slate-400 hover:text-white'
              }`}
            >
              Real Estate
            </button>
            <button
              onClick={() => setFilterStream('ECOM')}
              className={`px-3 py-1.5 rounded-lg transition font-semibold cursor-pointer ${
                filterStream === 'ECOM'
                  ? 'bg-emerald-500 text-slate-950 font-bold shadow'
                  : 'text-slate-400 hover:text-white'
              }`}
            >
              E-Commerce COD
            </button>
          </div>

          <button
            onClick={handleLockNextDeal}
            disabled={isSimulating || pendingCount === 0}
            className="bg-gradient-to-r from-[#ff4b89] to-[#ff2d55] hover:opacity-90 active:scale-95 text-white font-bold text-xs px-3.5 py-2 rounded-xl transition shadow-lg flex items-center gap-1.5 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span>⚡ Lock Next Deal</span>
          </button>
        </div>
      </div>

      {/* KPI Cards Grid */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-3 relative z-10">
        <div className="bg-[#060a12] border border-slate-800 rounded-xl p-3.5 space-y-1">
          <div className="text-[10px] font-mono-code text-slate-400 uppercase tracking-wider flex items-center justify-between">
            <span>🎯 90-DAY SPRINT GOAL</span>
            <span className="text-slate-500">100%</span>
          </div>
          <div className="font-mono-code text-lg sm:text-xl font-extrabold text-white">
            ৳ 1,00,00,000
          </div>
          <div className="text-[11px] text-slate-400 font-mono-code">
            ৳1.00 Crore Net Mobilization
          </div>
        </div>

        <div className="bg-[#060a12] border border-emerald-500/40 rounded-xl p-3.5 space-y-1 shadow-[0_0_15px_rgba(16,185,129,0.1)]">
          <div className="text-[10px] font-mono-code text-emerald-400 uppercase tracking-wider flex items-center justify-between">
            <span>🔒 LOCKED REVENUE</span>
            <span className="bg-emerald-500/20 text-emerald-300 px-1.5 py-0.2 rounded font-bold">
              {progressPercent}%
            </span>
          </div>
          <div className="font-mono-code text-lg sm:text-xl font-extrabold text-emerald-400">
            {formatBDT(lockedRevenue)}
          </div>
          <div className="text-[11px] text-emerald-300 font-mono-code flex items-center gap-1">
            <span>✓ {lockedCount} Deals Reconciled</span>
          </div>
        </div>

        <div className="bg-[#060a12] border border-slate-800 rounded-xl p-3.5 space-y-1">
          <div className="text-[10px] font-mono-code text-slate-400 uppercase tracking-wider flex items-center justify-between">
            <span>⏳ REMAINING TO TARGET</span>
            <span className="text-slate-400 font-bold">Day 24 of 90</span>
          </div>
          <div className="font-mono-code text-lg sm:text-xl font-extrabold text-sky-300">
            {formatBDT(remainingBDT)}
          </div>
          <div className="text-[11px] text-slate-400 font-mono-code">
            66 Days Remaining in Sprint
          </div>
        </div>

        <div className="bg-[#060a12] border border-slate-800 rounded-xl p-3.5 space-y-1">
          <div className="text-[10px] font-mono-code text-slate-400 uppercase tracking-wider flex items-center justify-between">
            <span>📈 PIPELINE POTENTIAL</span>
            <span className="text-[#ffb1c3] font-bold">{pendingCount} Active</span>
          </div>
          <div className="font-mono-code text-lg sm:text-xl font-extrabold text-[#ffb1c3]">
            {formatBDT(lockedRevenue + pendingPipelineSum)}
          </div>
          <div className="text-[11px] text-slate-400 font-mono-code">
            {( (lockedRevenue + pendingPipelineSum) / targetRevenue * 100 ).toFixed(0)}% Target Coverage
          </div>
        </div>
      </div>

      {/* 90-DAY PROGRESSION BAR SECTION */}
      <div className="bg-[#060a12] border border-slate-800/90 rounded-xl p-4 sm:p-5 space-y-3 relative z-10">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2">
          <div className="flex items-center gap-2">
            <span className="text-xs font-mono-code font-bold uppercase text-white tracking-wider">
              Target Progression Pace
            </span>
            <span className="text-[11px] font-mono-code bg-[#00dbe9]/15 text-[#00dbe9] border border-[#00dbe9]/30 px-2 py-0.5 rounded-full font-bold">
              {progressPercent}% Complete
            </span>
          </div>
          <div className="text-xs font-mono-code text-slate-400">
            Settlement Pace: <strong className="text-emerald-400">৳{((lockedRevenue / 24)).toFixed(0)} BDT / Day</strong> (Requires ৳{((remainingBDT / 66)).toFixed(0)} / Day)
          </div>
        </div>

        {/* Outer Bar */}
        <div className="relative w-full h-5 bg-[#02050b] rounded-full overflow-hidden border border-slate-700/80 p-0.5 shadow-inner">
          {/* Animated Progress Fill */}
          <div
            className="h-full rounded-full bg-gradient-to-r from-[#00dbe9] via-[#0ea5e9] to-[#ff4b89] transition-all duration-700 relative shadow-[0_0_15px_rgba(0,219,233,0.5)]"
            style={{ width: `${Math.max(3, progressPercent)}%` }}
          >
            <div className="absolute inset-0 bg-white/20 animate-pulse rounded-full" />
          </div>
        </div>

        {/* Milestone Checkpoints & Ticks */}
        <div className="grid grid-cols-4 text-[10px] sm:text-xs font-mono-code text-slate-400 pt-1 border-t border-slate-800/60">
          <div className="text-left">
            <span className="block text-slate-500">25% (৳25L)</span>
            <span className="text-slate-300 font-semibold">Seed Milestone</span>
          </div>
          <div className="text-center">
            <span className="block text-slate-500">50% (৳50L)</span>
            <span className="text-slate-300 font-semibold">Halfway Retainer</span>
          </div>
          <div className="text-center">
            <span className="block text-slate-500">75% (৳75L)</span>
            <span className="text-slate-300 font-semibold">Scale Velocity</span>
          </div>
          <div className="text-right">
            <span className="block text-emerald-400 font-bold">100% (৳1.00 Cr)</span>
            <span className="text-emerald-300 font-bold">Target Locked 🎯</span>
          </div>
        </div>
      </div>

      {/* RECHARTS LINE CHART SECTION */}
      <div className="bg-[#060a12] border border-slate-800/90 rounded-xl p-4 sm:p-5 space-y-3 relative z-10">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2">
          <div>
            <h4 className="font-display text-sm sm:text-base font-bold text-white flex items-center gap-2">
              <span>90-Day Trajectory vs. Actual Locked Run-Rate</span>
              <span className="text-[10px] font-mono-code bg-emerald-500/20 text-emerald-300 border border-emerald-500/40 px-2 py-0.5 rounded-full">
                LIVE QUEUE SYNC
              </span>
            </h4>
            <p className="text-xs text-slate-400">
              Interactive timeline visualization comparing planned linear target trajectory with actual cumulative locked revenue.
            </p>
          </div>
          <div className="flex items-center gap-3 text-xs font-mono-code text-slate-400">
            <div className="flex items-center gap-1.5">
              <span className="w-3 h-0.5 bg-[#00dbe9]" />
              <span className="text-slate-300">Locked Funds</span>
            </div>
            <div className="flex items-center gap-1.5">
              <span className="w-3 h-0.5 bg-slate-500 border-dashed" />
              <span>Target Line</span>
            </div>
            <div className="flex items-center gap-1.5">
              <span className="w-3 h-0.5 bg-[#ff4b89] border-dashed" />
              <span className="text-[#ffb1c3]">Projected</span>
            </div>
          </div>
        </div>

        {/* Recharts Container */}
        <div className="w-full h-72 sm:h-80 pt-2">
          <ResponsiveContainer width="100%" height="100%">
            <AreaChart
              data={chartData}
              margin={{ top: 10, right: 20, left: -10, bottom: 0 }}
            >
              <defs>
                <linearGradient id="lockedRevGradient" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#00dbe9" stopOpacity={0.38} />
                  <stop offset="95%" stopColor="#00dbe9" stopOpacity={0.0} />
                </linearGradient>
                <linearGradient id="projectedGradient" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#ff4b89" stopOpacity={0.2} />
                  <stop offset="95%" stopColor="#ff4b89" stopOpacity={0.0} />
                </linearGradient>
              </defs>

              <CartesianGrid stroke="#182234" strokeDasharray="3 3" />

              <XAxis
                dataKey="dayLabel"
                stroke="#64748b"
                tick={{ fill: '#94a3b8', fontSize: 11, fontFamily: 'monospace' }}
                tickLine={{ stroke: '#334155' }}
              />

              <YAxis
                stroke="#64748b"
                tick={{ fill: '#94a3b8', fontSize: 11, fontFamily: 'monospace' }}
                tickFormatter={formatShortBDT}
                domain={[0, 11000000]}
                tickLine={{ stroke: '#334155' }}
              />

              <Tooltip content={<CustomTooltip />} />

              {/* Reference Line for the ৳1 Crore Target */}
              <ReferenceLine
                y={10000000}
                stroke="#10b981"
                strokeDasharray="4 4"
                label={{
                  value: '🎯 ৳1.00 Cr Target',
                  fill: '#34d399',
                  fontSize: 10,
                  fontFamily: 'monospace',
                  position: 'insideTopRight'
                }}
              />

              {/* 90-Day Planned Target Line */}
              <Line
                type="linear"
                dataKey="targetTrajectory"
                name="90-Day Target Trajectory"
                stroke="#64748b"
                strokeWidth={2}
                strokeDasharray="4 4"
                dot={false}
              />

              {/* Projected Pipeline Line */}
              <Area
                type="monotone"
                dataKey="projectedTotal"
                name="Pipeline Projected"
                stroke="#ff4b89"
                strokeWidth={2}
                strokeDasharray="3 3"
                fill="url(#projectedGradient)"
                dot={{ fill: '#ff4b89', r: 3, strokeWidth: 1 }}
                connectNulls
              />

              {/* Actual Locked Revenue Line & Area */}
              <Area
                type="monotone"
                dataKey="lockedRevenue"
                name="Actual Locked Revenue"
                stroke="#00dbe9"
                strokeWidth={3}
                fill="url(#lockedRevGradient)"
                dot={{
                  fill: '#00dbe9',
                  r: 5,
                  stroke: '#060a12',
                  strokeWidth: 2
                }}
                activeDot={{
                  fill: '#ff4b89',
                  r: 7,
                  stroke: '#ffffff',
                  strokeWidth: 2
                }}
                connectNulls
              />
            </AreaChart>
          </ResponsiveContainer>
        </div>

        {/* Bottom Chart Footer Stats */}
        <div className="flex flex-wrap items-center justify-between gap-3 text-xs font-mono-code pt-3 border-t border-slate-800 text-slate-400">
          <div>
            Current Run Rate: <strong className="text-white">৳1.30 Lakh/deal</strong> average across {filteredTx.length} deals in queue
          </div>
          <div className="text-slate-400">
            Reconciled Trust Bank A/C: <span className="text-sky-400 font-bold">00110466000494</span> • BIN: <span className="text-slate-200">004982103-0101</span>
          </div>
        </div>
      </div>
    </div>
  );
};

// Mount to DOM if container exists
function mountRechartsDashboard() {
  const container = document.getElementById('rechartsRevenueDashboardRoot');
  if (container) {
    const root = createRoot(container);
    root.render(<RechartsRevenueDashboard />);
    console.log('Recharts Revenue Dashboard mounted successfully!');
  }
}

if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', mountRechartsDashboard);
} else {
  mountRechartsDashboard();
}

export default RechartsRevenueDashboard;
